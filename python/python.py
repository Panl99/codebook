import os

# 1、判断是否是一个目录
def isDir():
    path = ".\\"
    if os.path.isdir(path):
        print("%s is a dir" % path)
    else:
        print("%s is not a dir" % path)


def main():
    isDir()


if __name__ == '__main__':
    main()
