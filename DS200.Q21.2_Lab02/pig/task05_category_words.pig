%default FILE_TU_SACH '../output/kq_bai1_tokens'
%default OUT_TOP5_CHUNG '../output/kq_bai5_top5_tu_khoa'

du_lieu_tu_vung = LOAD '$FILE_TU_SACH' USING PigStorage(';') AS (id:int, aspect:chararray, category:chararray, sentiment:chararray, word:chararray);

gop_nhom_tu = GROUP du_lieu_tu_vung BY (category, word);
dem_tan_suat = FOREACH gop_nhom_tu GENERATE FLATTEN(group) AS (category, word), COUNT(du_lieu_tu_vung) AS so_lan_xuat_hien;

nhom_theo_cat = GROUP dem_tan_suat BY category;
top_5_chung = FOREACH nhom_theo_cat {
    ds_sap_xep = ORDER dem_tan_suat BY so_lan_xuat_hien DESC;
    lay_5_tu = LIMIT ds_sap_xep 5;
    GENERATE group AS the_loai, lay_5_tu.(word, so_lan_xuat_hien);
}
STORE top_5_chung INTO '$OUT_TOP5_CHUNG' USING PigStorage('\t');