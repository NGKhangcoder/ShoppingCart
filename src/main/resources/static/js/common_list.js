function showDeleteConfirmModal(link,entityName){
    entityId = link.attr("entityId");

    $("#yesButton").attr("href",link.attr("href"));
    $("#confirmText").text("Are you sure want to delete this " + entityName + " ID: " +  entityId + "?");
    $("#confirmModal").modal();
}