#!/bin/bash

# 生成静态调用关系表
java -jar javacg-0.1-SNAPSHOT-static.jar SimpleApp.jar > out.txt
cat out.txt | grep "M:aspectj.trace.core.app.SimpleApp" | grep -v "java." | grep -v "main"> callgraph.txt

# 生成中间调用关系
sed -E 's/[a-z]+\.//g;s/ / -> /; s/[\$|\:|M|(|)]|SimpleApp//g' callgraph.txt > callgraph2.txt

# 根据函数调用关系表生成调用图
OUT=callgraph.dot
echo 'digraph G {
    /*初始化节点和边的颜色*/
    node [peripheries=2 style=filled color="#eecc80"]
    edge [color="sienna" fontcolor="red"]' > $OUT
sed -E 's/[a-z]+\.//g;s/ / -> /; s/[\$|\:|M|(|)]|SimpleApp//g' callgraph.txt >> $OUT
echo "}" >> $OUT
dot -Tpng callgraph.dot -o callgraph.png
open callgraph.png