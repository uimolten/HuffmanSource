

Exerpiment Analysis:


The calgary collection (text files) compress much more compared to Waterloo (images). 
This probably is because Huffman coding works best when some byte values occur much more frequently than others, b
ecause those common values receive shorter codes.

The waterloo images compressed less because those images had lots of different colors and patterns which resulted in more distributed
byte frequencies, and less opportunities for shortening their codes.

Store Tree format tends to take longer to compress than Store Count format, more noticable on image files and when I read hf files instead of ignoring.

When we added already compressed files, the compression time roughly doubled, but the percent compression
was mostly less than double. 









Calgary times (SCF): 

read hf files
total bytes read: 5097064
total compressed bytes 3655370
total percent compression 28.285
compression time: 9.731

ignore hf files 
total bytes read: 3251493
total compressed bytes 1845571
total percent compression 43.239
compression time: 5.030


STF: 
Ignore:
total bytes read: 3251493
total compressed bytes 1830237
total percent compression 43.711
compression time: 4.925

read hf files:
total bytes read: 6866341
total compressed bytes 5405137
total percent compression 21.281
compression time: 14.206




Waterloo (SCF) :

ignore hf files: 
total bytes read: 12466304
total compressed bytes 10205282
total percent compression 18.137
compression time: 20.024


read hf files:
total bytes read: 22671586
total compressed bytes 20268639
total percent compression 10.599
compression time: 48.018


STF: 
ignore hf files: 
total bytes read: 12466304
total compressed bytes 10199808
total percent compression 18.181
compression time: 21.389

read hf files;
total bytes read: 38665706
total compressed bytes 36249209
total percent compression 6.250
compression time: 79.024



