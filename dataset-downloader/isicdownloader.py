import requests
import os
import time
import threading


class Downloader:
    URL = "https://isic-archive.com/api/v1"
    username = os.getenv("USERNAME")
    password = os.getenv("PASSWORD")

    def __init__(self):
        self.authToken = self._login(self.username, self.password)
        httpget = self.get("image?limit=60000")
        j = httpget.json()
        qtd = len(j)
        quarter = int(len(j) / 4)
        j1 = j[:quarter]
        j2 = j[quarter: quarter * 2]
        j3 = j[quarter * 2: quarter * 3]
        j4 = j[quarter * 3:]
        print('found %d images' % qtd)
        time.sleep(3)
        threading.Thread(target=self.work, args=(j1, )).start()
        threading.Thread(target=self.work, args=(j2, )).start()
        threading.Thread(target=self.work, args=(j3, )).start()
        threading.Thread(target=self.work, args=(j4, )).start()

    def work(self, j):
        for img in j:
            while True:
                try:
                    if os.path.exists('D:\\isic\\benign\\' + img['name'] + '.jpg') or os.path.exists('D:\\isic\\malignant\\' + img['name'] + '.jpg'):
                        print(img['name'] + '.jpg exists. Passing...')
                        break
                    try:
                        folder = self.get("image/%s" % img["_id"]).json()["meta"]['clinical']['benign_malignant']
                    except KeyError:
                        print('Found %s as unknown' % img["name"])
                        break
                    #if folder != "benign" and folder != "malignant":
                    if folder != "malignant":
                        break
                    print('Found %s as %s' % (img["name"], folder))
                    if not os.path.exists("D:\\isic\\" + folder):
                        os.mkdir("D:\\isic\\" + folder)
                    get_img = self.get("image/%s/download" % img["_id"])
                    if get_img.status_code != 200:
                        continue
                    with open("D:\\isic\\" + folder + '\\' + img['name'] + '.jpg', 'wb') as f:
                        for chunk in get_img:
                            f.write(chunk)
                    print("D:\\isic\\" + folder + '\\' + img['name'] + ".jpg saved.")
                    break
                except Exception as e:
                    print(str(e))

    def get(self, endpoint):
        url = self._makeUrl(endpoint)
        headers = {'Girder-Token': self.authToken} if self.authToken else None
        return requests.get(url, headers=headers)

    def _makeUrl(self, endpoint):
        return f'{self.URL}/{endpoint}'

    def _login(self, username, password):
        authResponse = requests.get(
            self._makeUrl('user/authentication'),
            auth=(username, password)
        )
        if not authResponse.ok:
            raise Exception(f'Login error: {authResponse.json()["message"]}')

        authToken = authResponse.json()['authToken']['token']
        return authToken


if __name__ == '__main__':
    Downloader()