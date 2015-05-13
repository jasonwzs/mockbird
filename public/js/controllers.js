/**
 */

angular.module('mockApp.controllers',['angularFileUpload']).controller('MockListController',function($scope,$state,popupService,$window,Mock,$upload){

    $scope.mocks=Mock.query();

    $scope.deleteMock=function(mock){
        if(popupService.showPopup('Really delete this?')){
            mock.$delete(function(){
                $window.location.href='';
            });
        }
    }

    $scope.downloadUri = SPA_CONSTANTS.downloadUri;

    $scope.upload = function (files) {
        if (files && files.length > 0) {
            if(popupService.showPopup('Import will delete all existing mocks. Continue?')){
                var file = files[0];
                $upload.http({
                    url: SPA_CONSTANTS.uploadUri,
                    data: file
                }).progress(function (evt) {
                    var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                    console.log('progress: ' + progressPercentage + '%');
                }).success(function (data, status, headers, config) {
                    console.log('file uploaded. Response: ' + data);
                    $window.location.href='';
                }).error(function (data, status, headers, config) {
                    console.log('file upload failed. Response: ' + data);
                    $window.location.href='';
                });
            }
        }
    }

}).controller('MockCreateController',function($scope,$state,$stateParams,Mock){

    $scope.mock=new Mock();
    $scope.mock.method = "GET";

    $scope.addMock=function(){
        $scope.mock.$save(function(){
            $state.go('mocks');
        });
    }

}).controller('MockEditController',function($scope,$state,$stateParams,Mock){

    $scope.updateMock=function(){
        $scope.mock.$update(function(){
            $state.go('mocks');
        });
    };

    $scope.loadMock=function(){
        $scope.mock=Mock.get({id:$stateParams.id});
    };

    $scope.loadMock();
});