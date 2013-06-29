/*jshint globalstrict: true*/
"use strict";
var app = angular.module("blog", ["ngResource"])
    .config(["$routeProvider", function($routeProvider) {
    // WARNING!
    // Never use a route starting with "/views/" or "/api/" or "/assets/"
    // For templateUrl, always start with "/views/"
    return $routeProvider.when("/", {
        templateUrl: "/views/index"
    }).when("/blogs", {
            templateUrl: "/views/blogs", controller: "BlogCtrl"
    }).when("/admin/new", {
            templateUrl: "/views/newBlog", controller: "AdminCtrl"
     }).otherwise({
            redirectTo: "/"
    });
}
])
    .config(["$locationProvider", function($locationProvider) {
    return $locationProvider.html5Mode(true).hashPrefix("!");
}
]);

function BlogCtrl($scope, $http) {
    $scope.loaded = false
    var url = '/api/blogs';
    $http.get(url).success(function(response) {
        if(response.status === 'OK') {
            $scope.blogs = response.data
            console.log($scope.blogs)
        }

    });
}

function AdminCtrl($scope) {

}

