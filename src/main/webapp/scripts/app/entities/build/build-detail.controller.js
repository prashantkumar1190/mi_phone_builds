'use strict';

angular.module('blrpoolApp')
    .controller('BuildDetailController', function ($scope, $rootScope, $stateParams, entity, Build) {
        $scope.build = entity;
        $scope.load = function (id) {
            Build.get({id: id}, function(result) {
                $scope.build = result;
            });
        };
        var unsubscribe = $rootScope.$on('blrpoolApp:buildUpdate', function(event, result) {
            $scope.build = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
