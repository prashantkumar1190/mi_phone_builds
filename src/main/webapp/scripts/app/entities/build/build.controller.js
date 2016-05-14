'use strict';

angular.module('blrpoolApp')
    .controller('BuildController', function ($scope, $state, Build, BuildSearch, ParseLinks) {

        $scope.builds = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Build.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.builds = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            BuildSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.builds = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.build = {
                device: null,
                build_type: null,
                build_path: null,
                build_name: null,
                sha1: null,
                dt_added: null,
                time_added: null,
                id: null
            };
        };
    });
