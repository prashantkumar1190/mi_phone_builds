'use strict';

angular.module('blrpoolApp')
	.controller('BuildDeleteController', function($scope, $uibModalInstance, entity, Build) {

        $scope.build = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Build.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
