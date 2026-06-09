"use strict";

$(function () {
    // フォームが送信（submit）された瞬間をキャッチ
    $('form').on('submit', function () {
        let $btn = $('#submit-btn');

        // ボタンを無効化（disabled）して、文字を「処理中...」に変える
        $btn.prop('disabled', true).text('処理中...');
    });
});