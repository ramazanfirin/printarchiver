(function() {
    'use strict';
    angular
        .module('printarchiverApp')
        .factory('PrintJobPage', PrintJobPage);

    PrintJobPage.$inject = ['$resource'];

    function PrintJobPage ($resource) {
        var resourceUrl =  'api/print-job-pages/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
