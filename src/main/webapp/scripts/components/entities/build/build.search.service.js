'use strict';

angular.module('blrpoolApp')
    .factory('BuildSearch', function ($resource) {
        return $resource('api/_search/builds/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
