'use strict';

angular.module('blrpoolApp')
    .factory('Build', function ($resource, DateUtils) {
        return $resource('api/builds/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dt_added = DateUtils.convertLocaleDateFromServer(data.dt_added);
                    data.time_added = DateUtils.convertDateTimeFromServer(data.time_added);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dt_added = DateUtils.convertLocaleDateToServer(data.dt_added);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dt_added = DateUtils.convertLocaleDateToServer(data.dt_added);
                    return angular.toJson(data);
                }
            }
        });
    });
