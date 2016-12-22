#!/bin/bash

# 生成静态调用关系表
java -jar javacg-0.1-SNAPSHOT-static.jar SimpleApp.jar > out.txt
cat out.txt | grep "aspectj.trace.core.app.SimpleApp" | grep -v "java." > callgraph.txt

# 生成中间调用关系
sed -E 's/[a-z]+\.//g;' callgraph.txt > callgraph2.txt

# 根据函数调用关系表生成调用图
OUT=callgraph.dot
echo "graph test {" > $OUT
sed -E 's/[a-z]+\.//g; s/[0-9]+\/;/; s/ / -- /; s/[\$|\:]/_/g'  callgraph.txt >> $OUT
echo "}" >> $OUT
dot -Tpng callgraph.dot -o callgraph.png