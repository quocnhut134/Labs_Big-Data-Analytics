%default FILE_GOC '../data/hotel-review.csv'
%default FILE_TU_SACH '../output/kq_bai1_tokens'

%default OUT_TU_PHOBIEN '../output/kq_bai2_tu_pho_bien'
%default OUT_THE_LOAI '../output/kq_bai2_the_loai'
%default OUT_KHIA_CANH '../output/kq_bai2_khia_canh'

bang_danh_gia = LOAD '$FILE_GOC' USING PigStorage(';') AS (id:int, comment:chararray, aspect:chararray, category:chararray, sentiment:chararray);
du_lieu_tu_vung = LOAD '$FILE_TU_SACH' USING PigStorage(';') AS (id:int, aspect:chararray, category:chararray, sentiment:chararray, word:chararray);

nhom_tu = GROUP du_lieu_tu_vung BY word;
dem_tu = FOREACH nhom_tu GENERATE group AS tu_vung, COUNT(du_lieu_tu_vung) AS so_lan;
tu_tren_500 = FILTER dem_tu BY so_lan > 500;
STORE tu_tren_500 INTO '$OUT_TU_PHOBIEN' USING PigStorage('\t');

nhom_the_loai = GROUP bang_danh_gia BY category;
dem_the_loai = FOREACH nhom_the_loai GENERATE group AS the_loai, COUNT(bang_danh_gia) AS tong_binh_luan;
STORE dem_the_loai INTO '$OUT_THE_LOAI' USING PigStorage('\t');

nhom_khia_canh = GROUP bang_danh_gia BY aspect;
dem_khia_canh = FOREACH nhom_khia_canh GENERATE group AS khia_canh, COUNT(bang_danh_gia) AS tong_binh_luan;
STORE dem_khia_canh INTO '$OUT_KHIA_CANH' USING PigStorage('\t');