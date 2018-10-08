import flask
from flask import request,Response,session
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy import *
import json
import copy
app = flask.Flask(__name__)
cur_dir=str(app.root_path)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///'+cur_dir+'demo.db'
db=SQLAlchemy(app)
#定义数据库表
#数据库建表
#1
#学生表数据提前导入
class Student(db.Model):
    stu_id=Column(Integer,primary_key=True,index=True)
    group_id=Column(Integer,ForeignKey('group.group_id'),index=True)
    stu_name=Column(String(80))
    password=Column(String(20))
    def __init__(self,stu_id,stu_name,password):
        self.stu_id=stu_id
        self.stu_name=stu_name
        self.password=password
#2
#教师表数据提前导入
class Teacher(db.Model):
    teacher_id=Column(Integer,primary_key=True,index=True)
    teacher_name=Column(String(80))
    password=Column(String(20))
    def __init__(self,teacher_id,teacher_name,password):
        self.teacher_id=teacher_id
        self.teacher_name=teacher_name
        self.password=password
    pass
#3
#课程表数据提前导入
class Course(db.Model):
    course_id=Column(Integer,primary_key=True,index=True)
    course_name=Column(String(20))
    def __init__(self,course_id,course_name):
        self.course_id=course_id
        self.course_name=course_name
    def get_dict(self):
        d={}
        d['course_id']=str(self.course_id)
        d['course_name']=str(self.course_name)
        return d
#4
class Inclass(db.Model):
    #自增主键
    inclass_id=Column(Integer,primary_key=True,autoincrement=True,index=True)
    course_id=Column(Integer,ForeignKey('course.course_id'),index=True)
    teacher_id=Column(Integer,ForeignKey('teacher.teacher_id'),index=True)
    inclass_name=Column(String(80))
    is_active=Column(Boolean)
    is_visible=Column(Boolean)
    d={}
    def __init__(self,course_id,inclass_name,teacher_id):
        self.course_id=course_id
        self.inclass_name=inclass_name
        self.teacher_id=teacher_id
        self.is_active=False
        self.is_visible=True
    def get_dict(self):
        self.d['inclass_id']=str(self.inclass_id)
        self.d['inclass_name']=str(self.inclass_name)
        self.d['course_id']=str(self.course_id)
        self.d['teacher_id']=str(self.teacher_id)
        self.d['is_active']=str(self.is_active)
        self.d['is_visible']=str(self.is_visible)
        return self.d


#5
#问题可以导入
class Question(db.Model):
    #自增主键
    question_id=Column(Integer,primary_key=True,autoincrement=True,index=True)
    teacher_id=Column(Integer,ForeignKey('teacher.teacher_id'),index=True)
    course_id=Column(Integer,ForeignKey('course.course_id'),index=True)
    question_content=Column(String(200))
    inclass_id=Column(Integer)
    def __init__(self,teacher_id,course_id,question_content,inclass_id):
        self.teacher_id=teacher_id
        self.course_id=course_id
        self.question_content=question_content
        self.inclass_id=inclass_id
    def get_dict(self):
        d={}
        d['question_id']=self.question_id
        d['teacher_id']=self.teacher_id
        d['course_id']=self.course_id
        d['question_content']=self.question_content
        return d
#6
class Class_question(db.Model):
    #自增主键
    class_question_id=Column(Integer,primary_key=True,autoincrement=True,index=True)
    question_id=Column(Integer,ForeignKey('question.question_id'),index=True)
    inclass_id=Column(Integer)
    def __init__(self,question_id,inclass_id):
        self.question_id=question_id
        self.inclass_id=inclass_id
#7
#课程安排提前导入
class Course_arrange(db.Model):
    #自增主键
    course_arrange_id=Column(Integer,primary_key=True,autoincrement=True,index=True)
    teacher_id=Column(Integer,ForeignKey('teacher.teacher_id'),index=True)
    course_id=Column(Integer,ForeignKey('course.course_id'),index=True)
    def __init__(self,teacher_id,course_id):
        self.teacher_id=teacher_id
        self.course_id=course_id
#8
class Group(db.Model):
    #自增主键
    group_id=Column(Integer,primary_key=True,autoincrement=True,index=True)
    course_id=Column(Integer,ForeignKey('course.course_id'),index=True)
    group_name=Column(String(20))
    group_score=Column(Integer)
    capital_id=Column(Integer)
    project_name=Column(String(20))
    def __init__(self,course_id,project_name,capital_id):
        self.course_id=course_id
        self.project_name=project_name
        self.capital_id=capital_id
#9
class Inclass_score(db.Model):
    #自增主键
    inclass_score_id=Column(Integer,primary_key=True,autoincrement=True,index=True)
    inclass_id=Column(Integer,ForeignKey('inclass.inclass_id'),index=True)
    stu_id=Column(Integer,ForeignKey('student.stu_id'),index=True)
    right_num=Column(Integer)
    total_num=Column(Integer)
    def __init__(self,inclass_id,stu_id,right_num,total_num):
        self.inclass_id=inclass_id
        self.stu_id=stu_id
        self.right_num=right_num
        self.total_num=total_num

    def get_dict(self):
        d={}
        d['inclass_socre_id']=self.inclass_score_id
        d['inclass_id']=self.inclass_id
        d['stu_id']=self.stu_id
        d['right_num']=self.right_num
        d['total_num']=self.total_num
        return d

#10
class Total_score(db.Model):
    #自增主键
    total_score_id=Column(Integer,primary_key=True,autoincrement=True,index=True)
    stu_id=Column(Integer,ForeignKey('student.stu_id'),index=True)
    course_id=Column(Integer,ForeignKey('course.course_id'),index=True)
    total_score=Column(Integer)
    def __init__(self,stu_id,course_id,total_score):
        self.stu_id=stu_id
        self.course_id=course_id
        self.total_score=total_score

#11
class Mutal_comment(db.Model):
    #自增主键
    mutal_comment_id=Column(Integer,primary_key=True,autoincrement=True,index=True)
    group_id=Column(Integer,ForeignKey('group.group_id'),index=True)
    group_group_id=Column(Integer,ForeignKey('group.group_id'),index=True)
    comment=Column(String(140))
    score=Column(Integer)
    def __init__(self,group_id,group_group_id,comment,score):
        self.group_id=group_id
        self.group_group_id=group_group_id
        self.comment=comment
        self.score=score
#12
class Student_answer(db.Model):
    #自增主键
    student_answer_id=Column(Integer,primary_key=True,autoincrement=True,index=True)
    inclass_id=Column(Integer,ForeignKey('inclass.inclass_id'),index=True)
    stu_id=Column(Integer,ForeignKey('student.stu_id'),index=True)
    question_id=Column(Integer,ForeignKey('question.question_id'),index=True)
    right=Column(Boolean)
    def __init__(self,inclass_id,stu_id,question_id,right):
        self.inclass_id=inclass_id
        self.stu_id=stu_id
        self.question_id=question_id
        self.right=right
    def get_dict(self):
        d={}
        d['inclass_id']=self.inclass_id
        d['stu_id']=self.stu_id
        d['question_id']=self.question_id
        d['right']=self.right
        return d

@app.route('/')
def test():
    return 'hello world'

# @app.route('/post_test',methods=['POST'])
# def post_test():
#     print(request.form)
#     return 'done'
#
# @app.route('/get_student',methods=['POST'])
# def get_arrange():
#     results=db.session.query(Student).all()
#     for item in results:
#         print(item.stu_name)
#     return 'done'
#
#
#定义教师操作
#
#
#教师登录
@app.route('/teacher_log_in',methods=['POST'])
def teacher_log_in():
    teacher_id_v=int(request.form['teacher_id'])
    password_v=request.form['password']
    teacher_v=db.session.query(Teacher).filter(Teacher.teacher_id==teacher_id_v).first()
    if teacher_v.password==password_v:
        return 'sucessful'
    return 'fail'
#教师现有的课程
#返回一个list
@app.route('/teacher_have_course',methods=['POST'])
def teacher_have_course():
    teacher_id_v=int(request.form['teacher_id'])
    course_ids=db.session.query(Course_arrange.course_id).filter_by(teacher_id=teacher_id_v).all()
    course_list=[]
    for i in course_ids:
        course={}
        cid=int(i[0])
        course_v=db.session.query(Course).filter_by(course_id=cid).all()
        #course_list[str(cid)]=course_name_v[0][0]
        course_list.append(course_v[0].get_dict())
    print(course_list)
    return Response(json.dumps(course_list,ensure_ascii=False),  mimetype='application/json')
#教师现有的课堂
@app.route('/teacher_have_inclass',methods=['POST'])
def teacher_have_inclass():
    teacher_id_v=request.form['teacher_id']
    course_id_v=request.form['course_id']
    inclass_list=[]
    inclasses=db.session.query(Inclass).filter(Inclass.teacher_id==teacher_id_v \
                                               ,Inclass.course_id==course_id_v).all()
    for ic in inclasses:
        d=ic.get_dict()
        inclass_list.append(copy.deepcopy(d))
        print(d)
    print(inclass_list)
    return  Response(json.dumps(inclass_list,ensure_ascii=False),mimetype='application/json')

#教师某门课现有的题目集
@app.route('/teacher_have_questions',methods=['POST'])
def teacher_have_questions():
    teacher_id_v=request.form['teacher_id']
    course_id_v=request.form['course_id']
    questions_list=[]
    questions=db.session.query(Question).filter(Question.teacher_id==teacher_id_v \
                                                ,Question.course_id==course_id_v).all()
    for q in questions:
        questions_list.append(q.get_dict())
        print(q.get_dict())
    return Response(json.dumps(questions_list,ensure_ascii=False),mimetype='application/json')
#教师查看答题情况
#返回一个列表
@app.route('/teacher_get_student_answers',methods=['POST'])
def teacher_get_student_answers():
    inclass_id_v=request.form['inclass_id']
    #获取当前课堂下的所有问题
    questions=db.session.query(Question.question_id).filter(Question.inclass_id==inclass_id_v).all()
    #查询的结果是一个元组的列表
    print(questions)
    return_list=[]
    #获取每个问题的内容和答题情况
    for item in questions:
        d={}
        qid=int(item[0])
        #获得问题的内容
        q=db.session.query(Question).filter(Question.question_id==qid).first()
        #统计答题情况
        student_answer_list=db.session.query(Student_answer).filter(Student_answer.question_id==qid, \
                                                                 Student_answer.inclass_id==inclass_id_v).all()
        total_student_num=len(student_answer_list)
        right_student_num=0
        for item in student_answer_list:
            if item.right==True:
                right_student_num+=1
        d['question_info']=q.get_dict()
        d['total_student_num']=total_student_num
        d['right_student_num']=right_student_num
        return_list.append(d)
    return Response(json.dumps(return_list, ensure_ascii=False), mimetype='application/json')
#教师创建课堂
@app.route('/teacher_create_inclass',methods=['POST'])
def teacher_create_inclass():
    course_id_v=request.form['course_id']
    inclass_name_v=request.form['inclass_name']
    teacher_id_v = request.form['teacher_id']
    inclass=Inclass(course_id_v,inclass_name_v,teacher_id_v)
    db.session.add(inclass)
    db.session.commit()
    inclass_id_v=db.session.query(Inclass.inclass_id).filter(Inclass.teacher_id==int(teacher_id_v) \
                                                ,Inclass.inclass_name==inclass_name_v).first()

    return str(inclass_id_v[0])
#教师上传题目
@app.route('/teacher_upload_question',methods=['POST'])
def teacher_upload_question():
    teacher_id_v=request.form['teacher_id']
    course_id_v=request.form['course_id']
    question_content_v=request.form['question_content']
    inclass_id_v=request.form['inclass_id']
    #插入Question表
    question=Question(teacher_id_v,course_id_v,question_content_v,inclass_id_v)
    db.session.add(question)
    db.session.commit()
    #查询question_id
    question_id_v=db.session.query(Question.question_id).filer(Question.course_id==course_id_v, \
                                                               Question.teacher_id==teacher_id_v).first()
    question_id_v=int(question_id_v[0])
    #插入Class_question表
    class_question_v=Class_question(question_id_v,inclass_id_v)
    db.session.add(class_question_v)
    db.session.commit()
    return 'done'
#
#
#定义学生操作
#
#
#学生登录
@app.route('/student_log_in',methods=['POST'])
def student_log_in():
    student_id_v=int(request.form['student_id'])
    password_v=request.form['password']
    student_v=db.session.query(Student).filter(Student.stu_id==student_id_v).first()
    if student_v.password==password_v:
        return 'sucessful'
    return 'fail'
#学生所有的课程
@app.route('/student_have_course',methods=['POST'])
def student_have_course():
    student_id_v=int(request.form['student_id'])
    course_id_list=db.session.query(Total_score.course_id).filter(Total_score.stu_id==student_id_v).all()
    course_list=[]
    for i in course_id_list:
        course={}
        cid=int(i[0])
        course_v=db.session.query(Course).filter_by(course_id=cid).all()
        #course_list[str(cid)]=course_name_v[0][0]
        course_list.append(course_v[0].get_dict())
    print(course_list)
    return Response(json.dumps(course_list,ensure_ascii=False),  mimetype='application/json')
#学生获取当前课程的课堂
@app.route('/student_have_inclass',methods=['POST'])
def student_have_inclass():
    student_id_v=int(request.form['student_id'])
    course_id_v=int(request.form['course_id'])
    inclass_list=db.session.query(Inclass).filter(Inclass.course_id==course_id_v).all()
    inclass_json_list=[]
    for item in inclass_list:
        inclass_json_list.append(copy.deepcopy(item.get_dict()))
    print(inclass_json_list)
    return Response(json.dumps(inclass_json_list, ensure_ascii=False), mimetype='application/json')
#学生获取课堂练习问题
@app.route('/student_get_inclass_question',methods=['POST'])
def student_get_inclass_question():
    question_list=[]
    inclass_id_v=int(request.form['inclass_id'])
    questions=db.session.query(Question).filter(Question.inclass_id==inclass_id_v).all()
    for q in questions:
        question_list.append(copy.deepcopy(q.get_dict()))
    return Response(json.dumps(question_list, ensure_ascii=False), mimetype='application/json')
#学生获取历史课堂答题情况
@app.route('/student_get_inclass_score',methods=['POST'])
def student_get_inclass_score():
    inclass_id_v=int(request.form['inclass_id'])
    student_id_v=int(request.form['student_id'])
    obj=db.session.query(Inclass_score).filter(Inclass_score.inclass_id==inclass_id_v \
                                               ,Inclass_score.stu_id==student_id_v).first()
    return Response(json.dumps(obj.get_dict(), ensure_ascii=False), mimetype='application/json')
#学生上传答题情况
@app.route('/student_upload_answer',methods=['POST'])
def student_upload_answer():
    inclass_id_v=int(request.form['inclass_id'])
    student_id_v=int(request.form['student_id'])
    right_num_v=int(request.form['right_num'])
    total_num_v=int(request.form['total_num'])
    d=request.form.to_dict()
    question_result_list=json.loads(d['question_result_list'])
    print(d['question_result_list'])
    #插入inclass_score数据表
    score=Inclass_score(inclass_id_v,student_id_v,right_num_v,total_num_v)
    db.session.add(score)
    db.session.commit()
    #插入student_answer表
    for item in question_result_list:
        print(item['right'])
        obj=Student_answer(inclass_id_v,student_id_v,int(item['question_id']),item['right'])
        db.session.add(obj)
        db.session.commit()
    return 'done'

if __name__=='__main__':
    db.create_all()
    app.run('0.0.0.0', debug=True)