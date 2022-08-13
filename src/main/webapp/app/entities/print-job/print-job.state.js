(function() {
    'use strict';

    angular
        .module('printarchiverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('print-job', {
            parent: 'entity',
            url: '/print-job?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'printarchiverApp.printJob.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/print-job/print-jobs.html',
                    controller: 'PrintJobController',
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
                    $translatePartialLoader.addPart('printJob');
                    $translatePartialLoader.addPart('processStatus');
                    $translatePartialLoader.addPart('resultStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('print-job-detail', {
            parent: 'print-job',
            url: '/print-job/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'printarchiverApp.printJob.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/print-job/print-job-detail.html',
                    controller: 'PrintJobDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('printJob');
                    $translatePartialLoader.addPart('processStatus');
                    $translatePartialLoader.addPart('resultStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PrintJob', function($stateParams, PrintJob) {
                    return PrintJob.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'print-job',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('print-job-detail.edit', {
            parent: 'print-job-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/print-job/print-job-dialog.html',
                    controller: 'PrintJobDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PrintJob', function(PrintJob) {
                            return PrintJob.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('print-job.new', {
            parent: 'print-job',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/print-job/print-job-dialog.html',
                    controller: 'PrintJobDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                printerJobLogId: null,
                                printDate: null,
                                userId: null,
                                userName: null,
                                printerId: null,
                                printerName: null,
                                pageCount: null,
                                documentName: null,
                                clientMachine: null,
                                jobId: null,
                                archivePath: null,
                                processStatus: null,
                                resultStatus: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('print-job', null, { reload: 'print-job' });
                }, function() {
                    $state.go('print-job');
                });
            }]
        })
        .state('print-job.edit', {
            parent: 'print-job',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/print-job/print-job-dialog.html',
                    controller: 'PrintJobDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PrintJob', function(PrintJob) {
                            return PrintJob.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('print-job', null, { reload: 'print-job' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('print-job.delete', {
            parent: 'print-job',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/print-job/print-job-delete-dialog.html',
                    controller: 'PrintJobDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PrintJob', function(PrintJob) {
                            return PrintJob.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('print-job', null, { reload: 'print-job' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
