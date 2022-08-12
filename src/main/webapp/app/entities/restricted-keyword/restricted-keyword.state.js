(function() {
    'use strict';

    angular
        .module('printarchiverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('restricted-keyword', {
            parent: 'entity',
            url: '/restricted-keyword?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'printarchiverApp.restrictedKeyword.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/restricted-keyword/restricted-keywords.html',
                    controller: 'RestrictedKeywordController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('restrictedKeyword');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('restricted-keyword-detail', {
            parent: 'restricted-keyword',
            url: '/restricted-keyword/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'printarchiverApp.restrictedKeyword.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/restricted-keyword/restricted-keyword-detail.html',
                    controller: 'RestrictedKeywordDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('restrictedKeyword');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'RestrictedKeyword', function($stateParams, RestrictedKeyword) {
                    return RestrictedKeyword.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'restricted-keyword',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('restricted-keyword-detail.edit', {
            parent: 'restricted-keyword-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/restricted-keyword/restricted-keyword-dialog.html',
                    controller: 'RestrictedKeywordDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RestrictedKeyword', function(RestrictedKeyword) {
                            return RestrictedKeyword.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('restricted-keyword.new', {
            parent: 'restricted-keyword',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/restricted-keyword/restricted-keyword-dialog.html',
                    controller: 'RestrictedKeywordDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                keyword: null,
                                active: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('restricted-keyword', null, { reload: 'restricted-keyword' });
                }, function() {
                    $state.go('restricted-keyword');
                });
            }]
        })
        .state('restricted-keyword.edit', {
            parent: 'restricted-keyword',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/restricted-keyword/restricted-keyword-dialog.html',
                    controller: 'RestrictedKeywordDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RestrictedKeyword', function(RestrictedKeyword) {
                            return RestrictedKeyword.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('restricted-keyword', null, { reload: 'restricted-keyword' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('restricted-keyword.delete', {
            parent: 'restricted-keyword',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/restricted-keyword/restricted-keyword-delete-dialog.html',
                    controller: 'RestrictedKeywordDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RestrictedKeyword', function(RestrictedKeyword) {
                            return RestrictedKeyword.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('restricted-keyword', null, { reload: 'restricted-keyword' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
