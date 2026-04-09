#!/bin/bash

echo "================================"
echo "Họ và tên sinh viên: Dương Quốc Nhựt"
echo "Mã số sinh viên: 23521132"
echo "================================"

echo "Cleaning up old output directories..."
rm -rf output/kq_bai1_tokens
rm -rf output/kq_bai2_tu_pho_bien output/kq_bai2_the_loai output/kq_bai2_khia_canh
rm -rf output/kq_bai3_khia_canh_tieu_cuc output/kq_bai3_khia_canh_tich_cuc
rm -rf output/kq_bai4_top5_khen output/kq_bai4_top5_che
rm -rf output/kq_bai5_top5_tu_khoa

echo "-> Running Task 1..."tar -xzf pig-0.17.0.tar.gz
pig -x local pig/task01_preprocess.pig
echo "-> Running Task 2..."
pig -x local pig/task02_statistics.pig
echo "-> Running Task 3..."
pig -x local pig/task03_aspect_sentiment.pig
echo "-> Running Task 4..."
pig -x local pig/task04_sentiment_words.pig
echo "-> Running Task 5..."
pig -x local pig/task05_category_words.pig

echo "================================"
echo "Họ và tên sinh viên: Dương Quốc Nhựt"
echo "Mã số sinh viên: 23521132"
echo "================================"