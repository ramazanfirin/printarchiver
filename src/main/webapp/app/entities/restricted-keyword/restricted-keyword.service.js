(function() {
    'use strict';
    angular
        .module('printarchiverApp')
        .factory('RestrictedKeyword', RestrictedKeyword);

    RestrictedKeyword.$inject = ['$resource'];

    function RestrictedKeyword ($resource) {
        var resourceUrl =  'api/restricted-keywords/:id';

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
