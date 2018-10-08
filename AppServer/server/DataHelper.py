from server import RouteAndDB
# with open('student.txt','r')as f:
#     for line in f.readlines():
#         line=line.strip()
#         line=line.split(' ')
#         student=RouteAndDB.Student(int(line[0]),line[1],int(line[0]))
#         RouteAndDB.db.session.add(student)
#         RouteAndDB.db.session.commit()
#
# with open('teacher.txt','r')as f:
#     for line in f.readlines():
#         line = line.strip()
#         line=line.split(' ')
#         teacher=RouteAndDB.Teacher(int(line[0]),line[1],int(line[0]))
#         RouteAndDB.db.session.add(teacher)
#         RouteAndDB.db.session.commit()
#
# with open('course.txt','r')as f:
#     for line in f.readlines():
#         line = line.strip()
#         line=line.split(' ')
#         course=RouteAndDB.Course(int(line[0]),line[1])
#         RouteAndDB.db.session.add(course)
#         RouteAndDB.db.session.commit()
#
# with open('course_arrange.txt','r')as f:
#     for line in f.readlines():
#         line = line.strip()
#         line =line.split(' ')
#         arrange=RouteAndDB.Course_arrange(int(line[0]),int(line[1]))
#         RouteAndDB.db.session.add(arrange)
#         RouteAndDB.db.session.commit()

with open('question.txt','r')as f:
    for line in f.readlines():
        line = line.strip()
        line=line.split(' ')
        question=RouteAndDB.Question(int(line[0]),int(line[1]),line[2],line[3])
        RouteAndDB.db.session.add(question)
        RouteAndDB.db.session.commit()

