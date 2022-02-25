(function () {
  //非同期で従業員名前一覧を取得
  let names = [];
  $.ajax({
    url: "http://localhost:8080/employee/suggest",
    dataType: "json",
    async: true,
  })
    .done(function (data) {
      names = data;
      $("#name").autocomplete({
        source: names,
      });
    })
    .fail(function (XMLHttpRequest, textStatus, errorThrown) {
      console.log("XMLHttpRequest:" + XMLHttpRequest.status);
      console.log("textStatus    :" + textStatus);
      console.log("errorThrown   :" + errorThrown.message);
    });
})();
