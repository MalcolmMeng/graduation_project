# with open('student.txt','r') as f:
#     for line in f.readlines():
#         print(line.strip(),end=' ')
import json
d={'name':'mjj'}
l=json.dumps(d)
print(l)