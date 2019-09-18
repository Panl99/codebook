from ftplib import FTP

ip_address = 'xxx.xxx.x.x'
username = 'username'
password = 'password'
current_dir = '/Enter the directory here/'
receive_filename = 'Enter the location of the file'
send_filename = 'Enter the name of the file '

ftp = FTP(ip_address)
ftp.login(user=username, passwd=password)
ftp.cwd(current_dir)


def ReceiveFile():
    with open(receive_filename, 'wb') as LocalFile:
        ftp.retrbinary('RETR ' + receive_filename, LocalFile.write, 1024)
    ftp.quit()


def SendFile():
    with open(send_filename, 'rb') as LocalFile:
        ftp.storbinary('STOR ' + send_filename, LocalFile)
    ftp.quit()
