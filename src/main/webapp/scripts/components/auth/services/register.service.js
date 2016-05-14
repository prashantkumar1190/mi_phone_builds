'use strict';

angular.module('blrpoolApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


