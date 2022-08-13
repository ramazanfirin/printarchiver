(function() {
    'use strict';

    angular
        .module('printarchiverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('print-job-page', {
            parent: 'entity',
            url: '/print-job-page?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'printarchiverApp.printJobPage.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/print-job-page/print-job-pages.html',
                    controller: 'PrintJobPageController',
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
                    $translatePartialLoader.addPart('printJobPage');
                    $translatePartialLoader.addPart('resultStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('print-job-page-detail', {
            parent: 'print-job-page',
            url: '/print-job-page/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'printarchiverApp.printJobPage.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/print-job-page/print-job-page-detail.html',
                    controller: 'PrintJobPageDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('printJobPage');
                    $translatePartialLoader.addPart('resultStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PrintJobPage', function($stateParams, PrintJobPage) {
                    return PrintJobPage.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'print-job-page',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('print-job-page-detail.edit', {
            parent: 'print-job-page-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/print-job-page/print-job-page-dialog.html',
                    controller: 'PrintJobPageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PrintJobPage', function(PrintJobPage) {
                            return PrintJobPage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('print-job-page.new', {
            parent: 'print-job-page',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/print-job-page/print-job-page-dialog.html',
                    controller: 'PrintJobPageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                pageName: null,
                                pagePath: null,
                                index: null,
                                resultStatus: null,
                                restrictedKeywords: null,
                                processed: null,
                                fileName: null,
                                exportPath: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('print-job-page', null, { reload: 'print-job-page' });
                }, function() {
                    $state.go('print-job-page');
                });
            }]
        })
        .state('print-job-page.edit', {
            parent: 'print-job-page',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/print-job-page/print-job-page-dialog.html',
                    controller: 'PrintJobPageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PrintJobPage', function(PrintJobPage) {
                            return PrintJobPage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('print-job-page', null, { reload: 'print-job-page' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('print-job-page.delete', {
            parent: 'print-job-page',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/print-job-page/print-job-page-delete-dialog.html',
                    controller: 'PrintJobPageDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PrintJobPage', function(PrintJobPage) {
                            return PrintJobPage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('print-job-page', null, { reload: 'print-job-page' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
