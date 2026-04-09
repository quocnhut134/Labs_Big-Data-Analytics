%default FILE_TU_SACH '../output/kq_bai1_tokens'
%default OUT_TOP5_KHEN '../output/kq_bai4_top5_khen'
%default OUT_TOP5_CHE '../output/kq_bai4_top5_che'

du_lieu_tu_vung = LOAD '$FILE_TU_SACH' USING PigStorage(';') AS (id:int, aspect:chararray, category:chararray, sentiment:chararray, word:chararray);

tu_tich_cuc = FILTER du_lieu_tu_vung BY sentiment == 'positive';
gop_nhom_khen = GROUP tu_tich_cuc BY (category, word);
dem_tu_khen = FOREACH gop_nhom_khen GENERATE FLATTEN(group) AS (category, word), COUNT(tu_tich_cuc) AS tan_suat;

nhom_theo_cat_khen = GROUP dem_tu_khen BY category;
top_5_khen = FOREACH nhom_theo_cat_khen {
    ds_sap_xep = ORDER dem_tu_khen BY tan_suat DESC;
    lay_5_tu = LIMIT ds_sap_xep 5;
    GENERATE group AS the_loai, lay_5_tu.(word, tan_suat);
}
STORE top_5_khen INTO '$OUT_TOP5_KHEN' USING PigStorage('\t');

tu_tieu_cuc = FILTER du_lieu_tu_vung BY sentiment == 'negative';
gop_nhom_che = GROUP tu_tieu_cuc BY (category, word);
dem_tu_che = FOREACH gop_nhom_che GENERATE FLATTEN(group) AS (category, word), COUNT(tu_tieu_cuc) AS tan_suat;

nhom_theo_cat_che = GROUP dem_tu_che BY category;
top_5_che = FOREACH nhom_theo_cat_che {
    ds_sap_xep = ORDER dem_tu_che BY tan_suat DESC;
    lay_5_tu = LIMIT ds_sap_xep 5;
    GENERATE group AS the_loai, lay_5_tu.(word, tan_suat);
}
STORE top_5_che INTO '$OUT_TOP5_CHE' USING PigStorage('\t');