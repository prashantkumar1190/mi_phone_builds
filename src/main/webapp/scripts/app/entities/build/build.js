'use strict';

angular.module('blrpoolApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('build', {
                parent: 'entity',
                url: '/builds',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'blrpoolApp.build.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/build/builds.html',
                        controller: 'BuildController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('build');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('build.detail', {
                parent: 'entity',
                url: '/build/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'blrpoolApp.build.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/build/build-detail.html',
                        controller: 'BuildDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('build');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Build', function($stateParams, Build) {
                        return Build.get({id : $stateParams.id});
                    }]
                }
            })
            .state('build.new', {
                parent: 'build',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/build/build-dialog.html',
                        controller: 'BuildDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    device: null,
                                    build_type: null,
                                    build_path: null,
                                    build_name: null,
                                    sha1: null,
                                    dt_added: null,
                                    time_added: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('build', null, { reload: true });
                    }, function() {
                        $state.go('build');
                    })
                }]
            })
            .state('build.edit', {
                parent: 'build',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/build/build-dialog.html',
                        controller: 'BuildDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Build', function(Build) {
                                return Build.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('build', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('build.delete', {
                parent: 'build',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/build/build-delete-dialog.html',
                        controller: 'BuildDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Build', function(Build) {
                                return Build.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('build', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
