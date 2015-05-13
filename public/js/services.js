/**
 */

angular.module('mockApp.services',[]).factory('Mock',function($resource){
    return $resource(SPA_CONSTANTS.resourceUri + '/:id',{id:'@id'},{
        update: {
            method: 'PUT'
        }
    });
}).service('popupService',function($window){
    this.showPopup=function(message){
        return $window.confirm(message);
    }
});