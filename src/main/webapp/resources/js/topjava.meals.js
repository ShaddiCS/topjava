function updateFilteredTable() {
    $.ajax({
        type: "GET",
        url: "ajax/profile/meals/filter",
        data: $("#filter").serialize()
    }).done(updateTableByData);
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get("ajax/profile/meals/", updateTableByData);
}

$(function () {
    makeEditable({
        ajaxUrl: "ajax/profile/meals/",
        datatableApi: $("#datatable").DataTable({
            "ajax": {
                "url": "ajax/profile/meals/",
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (date, type, row) {
                        if (type === "display") {
                            return date.substring(0, 10) + " " + date.substring(11);
                        }
                        return date;
                    }
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false,
                    "render": renderEditBtn
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false,
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                if (!data.excess) {
                    $(row).attr("data-mealexcess", false);
                } else {
                    $(row).attr("data-mealexcess", true);
                }
            }
        }),
        updateTable: updateFilteredTable
    });
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

    $('#dateTime').datetimepicker({
        format:'Y-m-d\\TH:i'
    });
});