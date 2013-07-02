///*jshint globalstrict: true*/
//"use strict";
//var app = angular.module("blog", ["ngResource"])
//    .config(["$routeProvider", function($routeProvider) {
//    // WARNING!
//    // Never use a route starting with "/views/" or "/api/" or "/assets/"
//    // For templateUrl, always start with "/views/"
//    return $routeProvider.when("/", {
//        templateUrl: "/views/index"
//    }).when("/blogs", {
//            templateUrl: "/views/blogs", controller: "BlogCtrl"
//    }).when("/admin/test", {
//            templateUrl: "/views/addBlog", controller: "AdminCtrl"
//        }).otherwise({
//            redirectTo: "/"
//    });
//}
//])
//    .config(["$locationProvider", function($locationProvider) {
//    return $locationProvider.html5Mode(true).hashPrefix("!");
//}
//]);
//
//function BlogCtrl($scope, $http) {
//    $scope.loaded = false
//    var url = '/api/blogs';
//    $http.get(url).success(function(response) {
//        if(response.status === 'OK') {
//            $scope.blogs = response.data
//            console.log($scope.blogs[0])
//        }
//    });
//}
//
//function BlogOneCtrl($scope, $routeParams) {
//
//}
//
//function AdminCtrl($scope, $http) {
//    console.log('admin controller')
//    $scope.addBlog = function(blog) {
//        console.log('do I get called');
//        console.log(blog);
//        $http({
//            method: 'POST',
//            url: '/admin/new',
//            data: {'title': blog.title,
//                   'body': blog.body,
//                   'slug': blog.slug,
//                   'createdOn': 1
//            }
//        });
//    }
//}


