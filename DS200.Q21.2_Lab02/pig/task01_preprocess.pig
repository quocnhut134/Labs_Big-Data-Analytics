%default FILE_CSV './data/hotel-review.csv'
%default FILE_STOPWORDS './data/stopwords.txt'
%default THUMUC_DAURA './output/kq_bai1_tokens'

raw_data = LOAD '$FILE_CSV' USING PigStorage(';') AS (id:int, comment:chararray, aspect:chararray, category:chararray, sentiment:chararray);
danh_sach_tu_dung = LOAD '$FILE_STOPWORDS' AS (word:chararray);

chuoi_tu_vung = FOREACH raw_data GENERATE id, aspect, category, sentiment, FLATTEN(TOKENIZE(LOWER(comment))) AS word;

bang_ket_hop = JOIN chuoi_tu_vung BY word LEFT OUTER, danh_sach_tu_dung BY word;
du_lieu_sach = FILTER bang_ket_hop BY danh_sach_tu_dung::word IS NULL;

ket_qua_cuoi = FOREACH du_lieu_sach GENERATE chuoi_tu_vung::id AS id, chuoi_tu_vung::aspect AS aspect, chuoi_tu_vung::category AS category, chuoi_tu_vung::sentiment AS sentiment, chuoi_tu_vung::word AS word;

STORE ket_qua_cuoi INTO '$THUMUC_DAURA' USING PigStorage(';');