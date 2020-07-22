import os
import logging
import sys
import fileinput
import getpass
import subprocess

cur_path = os.getcwd()   #当前目录
path = ".\\"


# 1、判断是否是一个目录
def is_dir(cur_path):
    if os.path.isdir(cur_path):
        print("%s is a dir" % cur_path)
    else:
        print("%s is not a dir" % cur_path)

# 2、递归 遍历目录下文件列表
def get_file_list(cwd):
    result = []
    get_dir = os.listdir(cwd)  #遍历当前目录，获取文件列表
    for i in get_dir:
        sub_dir = os.path.join(cwd,i)  # 把第一步获取的文件加入路径
        if os.path.isdir(sub_dir):     #如果当前仍然是文件夹，递归调用
            get_file_list(sub_dir)
        else:
            ax = os.path.basename(sub_dir)  #如果当前路径不是文件夹，则把文件名放入列表
            result.append(ax)
    print(result)   #对列表计数
# 输出：['douban_movie.py', 'excelToDatabase.py', 'ftp_file_transfer.py', 'python.iml', 'python.md', 'python.py', 'readExcel.py', 'validate_filenames.py']

# 3、创建日志文件
def create_logger():
    logging.basicConfig(filename='python.log', level=logging.DEBUG, format='%(levelname)s %(asctime)s %(message)s')
    logger = logging.getLogger()
    logger.info("create logger success")

# 4、快速遍历一个或多个文件内容
def get_content(path):
    with fileinput.input(path) as f:
        for line in f:
            print(f.filename(), f.lineno(), line, end='')

# 5、终止程序并返回错误信息
# 打印错误日志 并返回非零状态码终止程序
def sys_exit():
    sys.stderr.write("error! system exit!")
    raise SystemExit(1)

# 6、交互式脚本在运行时需要一个密码。不能将密码在脚本中硬编码，而是需要弹出一个密码输入提示，让用户自己输入。
def get_pass():
    user = getpass.getuser() #不弹出用户名提示，须使用user = input('Enter your username: ')
    passwd = getpass.getpass()
    if check_pass(user, passwd):
        print("success!")
    else:
        print("failed!")
def check_pass(user, pwd):
    if user == 'Admain' and pwd == '123456':
        print(user+'---'+pwd)
    else:
        print(user+'---'+pwd)

# 7、执行外部命令并获取执行结果
def exec_command():
    try:
        # out_bytes = subprocess.check_output(['netstat', '-a']) #subprocess.getoutput()
        out_bytes = subprocess.check_output('whoami') #返回一个字符串
        out_text = out_bytes.decode('utf-8') #返回文本

        # 执行一个shell命令：out_bytes = subprocess.check_output('grep python | wc > out', shell=True)
        # 需要同时收集标准输出和错误输出时：out_bytes = subprocess.check_output(['cmd','arg1','arg2'], stderr=subprocess.STDOUT)

    # 被执行的命令返回非零码，就会抛出异常
    except subprocess.CalledProcessError as e:
        out_bytes = e.output
        code = e.returncode
    print(out_bytes)
    print(out_text)

    # 执行命令超时
    try:
        out_bytes = subprocess.check_output(['cmd','arg1','arg2'], timeout=5)
    except subprocess.TimeoutExpired as e:
        out_bytes = e.output
        code = e.returncode



def main():
    # is_dir(cur_path)
    # get_file_list(cur_path)
    # create_logger()
    # get_content(cur_path+'/'+'python.log')
    # sys_exit()
    # get_pass()
    # exec_command()






if __name__ == '__main__':
    main()
