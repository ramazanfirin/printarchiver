(function() {
    'use strict';

    angular
        .module('printarchiverApp')
        .factory('PasswordResetFinish', PasswordResetFinish);

    PasswordResetFinish.$inject = ['$resource'];

    function PasswordResetFinish($resource) {
        var service = $resource('api/account/reset-password/finish', {}, {});

        return service;
    }
})();
