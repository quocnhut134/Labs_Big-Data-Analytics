%default FILE_GOC './data/hotel-review.csv'
%default OUT_TIEU_CUC_NHAT './output/kq_bai3_khia_canh_tieu_cuc'
%default OUT_TICH_CUC_NHAT './output/kq_bai3_khia_canh_tich_cuc'

bang_danh_gia = LOAD '$FILE_GOC' USING PigStorage(';') AS (id:int, comment:chararray, aspect:chararray, category:chararray, sentiment:chararray);

danh_gia_tieu_cuc = FILTER bang_danh_gia BY sentiment == 'negative';
nhom_kc_tieu_cuc = GROUP danh_gia_tieu_cuc BY aspect;
dem_kc_tieu_cuc = FOREACH nhom_kc_tieu_cuc GENERATE group AS khia_canh, COUNT(danh_gia_tieu_cuc) AS so_lan_che;
sap_xep_tieu_cuc = ORDER dem_kc_tieu_cuc BY so_lan_che DESC;
STORE sap_xep_tieu_cuc INTO '$OUT_TIEU_CUC_NHAT' USING PigStorage('\t');

danh_gia_tich_cuc = FILTER bang_danh_gia BY sentiment == 'positive';
nhom_kc_tich_cuc = GROUP danh_gia_tich_cuc BY aspect;
dem_kc_tich_cuc = FOREACH nhom_kc_tich_cuc GENERATE group AS khia_canh, COUNT(danh_gia_tich_cuc) AS so_lan_khen;
sap_xep_tich_cuc = ORDER dem_kc_tich_cuc BY so_lan_khen DESC;
STORE sap_xep_tich_cuc INTO '$OUT_TICH_CUC_NHAT' USING PigStorage('\t');