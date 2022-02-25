(function () {
  const $addressSearchButton = document.getElementById("addressSearchButton");
  $addressSearchButton.addEventListener("click", async () => {
    console.log("クリックされた");
    console.log($("#address").val());
    $("#address").val("");
    await $.ajax({
      url: "http://zipcoda.net/api",
      dataType: "jsonp",
      data: {
        zipcode: $("#zipCode").val(),
      },
      async: true,
    })
      .done(function (data) {
        console.log("通信は成功");
        console.log(data);
        console.log($("#address").val(data.items[0].address));
        $("#address").val(data.items[0].address);
      })
      .fail(function (XMLHttpRequest, textStatus, errorThrown) {
        alert("正しい結果を得られませんでした");
        console.log("XMLHttpRequest:" + XMLHttpRequest.status);
        console.log("textStatus    :" + textStatus);
        console.log("errorThrown   :" + errorThrown.message);
      });
    console.log("到達しないコード");
    console.log($("#address").val());
    if ($("#address").val() == "") {
      console.log("住所空です");
      $("#address").val("存在しない郵便番号です");
    }
  });
})();
