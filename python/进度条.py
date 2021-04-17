import time

scale = 50

print(">>>>>>>>>>开始<<<<<<<<<<".center(scale // 2, "_"))

start = time.perf_counter()  # 计时

for i in range(scale + 1):
    a = '*' * i
    b = '.' * (scale - i)
    c = (i / scale) * 100
    dur = time.perf_counter() - start

    print("\r{:^3.0f} % [{} -> {}]{:.2f}S".format(c, a, b, dur), end='')

    time.sleep(0.1)

print("\n" + ">>>>>>>>>>结束<<<<<<<<<<".center(scale // 2, '_'))

# __>>>>>>>>>>开始<<<<<<<<<<_
# 100 % [************************************************** -> ]5.48S
# __>>>>>>>>>>结束<<<<<<<<<<_
