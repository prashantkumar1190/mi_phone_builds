'use strict';

angular.module('blrpoolApp').controller('BuildDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Build',
        function($scope, $stateParams, $uibModalInstance, entity, Build) {

        $scope.build = entity;
        $scope.load = function(id) {
            Build.get({id : id}, function(result) {
                $scope.build = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('blrpoolApp:buildUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.build.id != null) {
                Build.update($scope.build, onSaveSuccess, onSaveError);
            } else {
                Build.save($scope.build, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForDt_added = {};

        $scope.datePickerForDt_added.status = {
            opened: false
        };

        $scope.datePickerForDt_addedOpen = function($event) {
            $scope.datePickerForDt_added.status.opened = true;
        };
        $scope.datePickerForTime_added = {};

        $scope.datePickerForTime_added.status = {
            opened: false
        };

        $scope.datePickerForTime_addedOpen = function($event) {
            $scope.datePickerForTime_added.status.opened = true;
        };
}]);
