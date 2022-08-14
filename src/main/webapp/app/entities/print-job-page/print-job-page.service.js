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
            'findByJobId': { method: 'GET', isArray: true, url: 'api/print-job-pages/getByJobId'},
            'findContext': { method: 'GET', isArray: false, url: 'api/print-job-pages/getContent'},
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
