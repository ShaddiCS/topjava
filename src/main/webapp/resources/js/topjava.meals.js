$(function () {
    makeEditable({
            ajaxUrl: "ajax/meals/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "dateTime"
                    },
                    {
                        "data": "description"
                    },
                    {
                        "data": "calories"
                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    [
                        0,
                        "asc"
                    ]
                ]
            })
        }
    );
    $('#startDate').datetimepicker({
        format:'Y-m-d',
        timepicker:false
    });

    $('#endDate').datetimepicker({
        format:'Y-m-d',
        timepicker:false
    });

    $('#startTime').datetimepicker({
        format:'H:i',
        datepicker:false
    });

    $('#endTime').datetimepicker({
        format:'H:i',
        datepicker:false
    });
});

function updateFilteredTable() {

    $.get(context.ajaxUrl + "filter",
        {startDate: $("#startDate").val(), endDate: $("#endDate").val(), startTime: $("#startTime").val(), endTime: $("#endTime").val()},
        function(data) {
            context.datatableApi.clear().rows.add(data).draw();
        });
}

function clearFilter() {
    $(".form-control").val("");
    updateTable();
}