(function() {
    'use strict';
    angular
        .module('printarchiverApp')
        .factory('PrintJob', PrintJob);

    PrintJob.$inject = ['$resource', 'DateUtils'];

    function PrintJob ($resource, DateUtils) {
        var resourceUrl =  'api/print-jobs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.printDate = DateUtils.convertLocalDateFromServer(data.printDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.printDate = DateUtils.convertLocalDateToServer(copy.printDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.printDate = DateUtils.convertLocalDateToServer(copy.printDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
