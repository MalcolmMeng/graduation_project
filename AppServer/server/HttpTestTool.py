import requests
import json
def test_method(user_info,url):
    return requests.post(url,user_info)
url='http://127.0.0.1:5000/'
user_info={}
#教师登录
# user_info={'teacher_id':'1','password':'1'}
# print(test_method(user_info,url+'teacher_log_in').text)
#教师所有的课程
# user_info={'user':'ai','teacher_id':'2'}
# print(test_method(user_info,url+'teacher_have_course').text)
# #教师创建课堂
# user_info={'course_id':'1','inclass_name':'计算机组成原理第一周第三次','teacher_id':'1'}
# print(test_method(user_info,url+'teacher_create_inclass').text)
#教师所有的课堂
# user_info={'course_id':'1','teacher_id':'1'}
# s=test_method(user_info,url+'teacher_have_inclass').text
# d=json.loads(s)
# print(d)
# print(d[0]['is_active'])
#教师某门课程现有题目集
# user_info={'course_id':'1','teacher_id':'1'}
# print(test_method(user_info,url+'teacher_have_questions').text)
#教师查看答题情况
user_info={'inclass_id':'1'}
obj=json.loads(test_method(user_info,url+'teacher_get_student_answers').text)
print(obj)
for i in obj:
    print(i)

#
#
#学生测试
#学生所有的课程
# user_info={'student_id':'1'}
# print(test_method(user_info,url+'student_have_course').text)
#学生获取当前课程的课堂
# user_info={'student_id':'1','course_id':'1'}
# print(test_method(user_info,url+'student_have_inclass').text)
#学生获取课堂练习问题
# user_info={'inclass_id':'3'}
# obj=json.loads(test_method(user_info,url+'student_get_inclass_question').text)
# print(obj)
# for item in obj:
#     print(item)
#学生上传答题情况
# question_result_list=[{'question_id':'1','right':True},{'question_id':'2','right':True},{'question_id':'3','right':False}]
# question_result_list=json.dumps(question_result_list,ensure_ascii=False)
# user_info={'inclass_id':'2','student_id':'2','right_num':'2','total_num':'3','question_result_list':question_result_list}
# print(test_method(user_info,url+'student_upload_answer').text)


