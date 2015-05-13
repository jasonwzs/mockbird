/**
 */

angular.module('mockApp',['ui.router','ngResource', 'angular-loading-bar', 'ngAnimate', 'mockApp.controllers','mockApp.services',]);

angular.module('mockApp').config(function($stateProvider,$httpProvider){
    $stateProvider.state('mocks',{
        url:'/mocks',
        templateUrl:'partials/mocks.html',
        controller:'MockListController'
    }).state('newMock',{
        url:'/mocks/new',
        templateUrl:'partials/mock-add.html',
        controller:'MockCreateController'
    }).state('editMock',{
        url:'/mocks/:id/edit',
        templateUrl:'partials/mock-edit.html',
        controller:'MockEditController'
    });
}).run(function($state){
   $state.go('mocks');
});