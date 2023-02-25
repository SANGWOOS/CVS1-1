# Merged Version
import csv
import json
import time
import re
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.common import NoSuchElementException, StaleElementReferenceException
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from threading import Thread
from webdriver_manager.chrome import ChromeDriverManager

final = []
gs_11 = {
    "brand": "GS25",
    "type": "1+1",
    "prod_list": []
}
gs_21 = {
    "brand": "GS25",
    "type": "2+1",
    "prod_list": []
}
se_11 = {
    "brand": "SE",
    "type": "1+1",
    "prod_list": []
}
se_21 = {
    "brand": "SE",
    "type": "2+1",
    "prod_list": []
}
em_11 = {
    "brand": "emart24",
    "type": "1+1",
    "prod_list": []
}
em_21 = {
    "brand": "emart24",
    "type": "2+1",
    "prod_list": []
}
cu_11 = {
    "brand": "CU",
    "type": "1+1",
    "prod_list": []
}
cu_21 = {
    "brand": "CU",
    "type": "2+1",
    "prod_list": []
}


def make_driver():
    options = webdriver.ChromeOptions()
    options.add_argument('--headless')
    options.add_argument('--start-maximized')
    options.add_argument('--window-size=1920,1080')
    options.add_argument("disable-gpu")
    service = Service(executable_path=ChromeDriverManager().install())
    driver = webdriver.Chrome(service=service, options=options)
    return driver


def gs_worker(tid):
    driver = make_driver()
    driver.get('http://gs25.gsretail.com/gscvs/ko/products/event-goods')
    wait = WebDriverWait(driver, timeout=100, poll_frequency=1,
                         ignored_exceptions=[NoSuchElementException, StaleElementReferenceException])

    if tid == 2:
        wait.until(EC.element_to_be_clickable((By.ID, "TWO_TO_ONE")))
        driver.find_element(By.ID, 'TWO_TO_ONE').click()
        time.sleep(3)

    source = driver.page_source
    p = re.compile(
        'a href="#next2;" onclick="goodsPageController.movePage\(([0-9]+)\)" title="마지막 페이지 보기" class="next2"')
    result = p.findall(source)
    end_page = int(result[0])

    prev = ""
    for i in range(1, end_page + 1):
        partial_res = []
        while True:
            wait.until(
                EC.visibility_of_element_located((By.XPATH, '//*[@id="contents"]/div[2]/div[3]/div/div/div[{}]/ul'.format(tid))))
            outer = driver.find_element(By.XPATH, '//*[@id="contents"]/div[2]/div[3]/div/div/div[{}]/ul'.format(tid))
            inner = outer.find_elements(By.CLASS_NAME, 'prod_box')
            for j in range(len(inner)):
                EC.presence_of_element_located((By.XPATH, '//*[@id="contents"]/div[2]/div[3]/div/div/div[{0}]/ul/li[{1}]/div/p[1]/img'.format(tid, j+1)))

            try:
                tmp_str = inner[0].text
                if tmp_str is None:
                    continue
                if len(tmp_str) > 0:
                    if prev == tmp_str:
                        continue
                    else:
                        prev = tmp_str
                        partial_res = inner
                        break
            except (NoSuchElementException, StaleElementReferenceException):
                continue
        for idx, p in enumerate(partial_res, 1):
            t = p.text.split('\n')
            t[1] = t[1].replace(",", "").replace("원", "")
            url = ""
            try:
                url = p.find_element(By.XPATH, '//*[@id="contents"]/div[2]/div[3]/div/div/div[{0}]/ul/li[{1}]/div/p[1]/img'.format(tid, idx)).get_attribute('src')
            except NoSuchElementException:
                url = ""
            if tid == 1:
                gs_11['prod_list'].append({
                    "name": t[0],
                    "price": t[1],
                    "image": url
                })
            elif tid == 2:
                gs_21['prod_list'].append({
                    "name": t[0],
                    "price": t[1],
                    "image": url
                })
        if i != end_page: driver.execute_script("goodsPageController.movePage({})".format(i + 1))

    driver.quit()
    return


def cu_worker(tid):
    driver = make_driver()
    driver.get('https://cu.bgfretail.com/event/plus.do?category=event&depth2=1&sf=N')
    start = 1
    end = 30

    if tid == 1:
        driver.execute_script('javascript:goDepth("23")')
    elif tid == 2:
        driver.execute_script('javascript:goDepth("24")')
    else:
        print('error thread')
        driver.quit()
        return
    time.sleep(20)

    cnt = 0
    for i in range(start, end):
        flag = False
        while True:
            try:
                items = driver.find_elements(By.CLASS_NAME, 'prod_item')
                if len(items) > 0 and len(
                        driver.execute_script("return document.getElementsByClassName('prodListBtn')")) == 0:
                    flag = True
                    break
                if len(items) > cnt:
                    cnt = len(items)
                    break
                else:
                    continue
            except (NoSuchElementException, StaleElementReferenceException):
                continue
        if flag:
            break
        driver.execute_script("nextPage(1)")

    res = driver.find_elements(By.CLASS_NAME, "prod_item")
    for idx, item in enumerate(res, 1):
        if tid == 1:
            cu_11['prod_list'].append({
                'name': item.find_element(By.CLASS_NAME, 'name').text,
                'price': item.find_element(By.CLASS_NAME, 'price').text.replace(",", "").replace("원", "").replace("\n", ""),
                'image': item.find_element(By.CLASS_NAME, 'prod_img').find_element(By.CLASS_NAME, 'prod_img').get_attribute('src')
            })
        elif tid == 2:
            cu_21['prod_list'].append({
                'name': item.find_element(By.CLASS_NAME, 'name').text,
                'price': item.find_element(By.CLASS_NAME, 'price').text.replace(",", "").replace("원", "").replace("\n", ""),
                'image': item.find_element(By.CLASS_NAME, 'prod_img').find_element(By.CLASS_NAME,
                                                                                   'prod_img').get_attribute('src')
            })

    driver.quit()
    return


def se_worker(tid):
    driver = make_driver()
    driver.get('https://www.7-eleven.co.kr/product/presentList.asp')

    driver.execute_script("fncTab('{}')".format(tid))
    time.sleep(3)
    cnt_items = 13
    source = driver.page_source
    p = re.compile('var intTotalCount = "([0-9]+)"')
    result = p.findall(source)
    end_page = (int(result[0]) - 13) // 10

    for i in range(end_page + 1):
        driver.execute_script("fncMore('{}')".format(tid))
        while driver.execute_script("return $('#listCnt').val()") is None:
            print('', end='')
        while 1 <= int(driver.execute_script("return $('#listCnt').val()")) <= cnt_items:
            print('', end='')
        cnt_items = int(driver.execute_script("return $('#listCnt').val()"))
        if cnt_items == 0:
            break
    items = driver.find_elements(By.CLASS_NAME, 'infowrap')

    for idx, item in enumerate(items, 1):
        if tid == 1:
            se_11['prod_list'].append({
                'name': item.find_element(By.CLASS_NAME, 'name').text,
                'price': item.find_element(By.CLASS_NAME, 'price').text.replace(",", ""),
                'image': item.find_element(By.XPATH, '..').find_element(By.TAG_NAME, 'img').get_attribute('src')
            })
        elif tid == 2:
            se_21['prod_list'].append({
                'name': item.find_element(By.CLASS_NAME, 'name').text,
                'price': item.find_element(By.CLASS_NAME, 'price').text.replace(",", ""),
                'image': item.find_element(By.XPATH, '..').find_element(By.TAG_NAME, 'img').get_attribute('src')
            })

    driver.quit()
    return


def em_worker(tid):
    driver = make_driver()
    wait = WebDriverWait(driver, timeout=100, poll_frequency=1,
                         ignored_exceptions=[NoSuchElementException, StaleElementReferenceException])

    if tid == 1:
        driver.get('https://emart24.co.kr/goods/event?search=&category_seq=1&align=')
    elif tid == 2:
        driver.get('https://emart24.co.kr/goods/event?search=&category_seq=2&align=')
    else:
        driver.quit()
        return
    time.sleep(3)

    source = driver.page_source
    p = re.compile('const totalCount = \"([0-9]+)\";')
    result = p.findall(source)
    total_count = int(result[0])
    end_page = 0

    if total_count % 20 == 0:
        end_page = total_count // 20
    else:
        end_page = 1 + total_count // 20

    prev = ""
    for i in range(1, end_page + 1):
        partial_res = []
        while True:
            wait.until(
                EC.visibility_of_all_elements_located((By.XPATH, '/html/body/div[2]/div/section[4]/div')))
            items = driver.find_elements(By.XPATH, '/html/body/div[2]/div/section[4]/div')
            try:
                tmp_str = items[0].text
                if tmp_str is None:
                    continue
                if len(tmp_str) > 0:
                    if prev == tmp_str:
                        continue
                    else:
                        prev = tmp_str
                        partial_res = items
                        break
            except (NoSuchElementException, StaleElementReferenceException):
                continue
        for p in partial_res:
            t = p.find_element(By.CLASS_NAME, 'itemTxtWrap').text.split('\n')
            t[1] = t[1].replace(" 원", "").replace(",", "")
            url = ""
            try:
                url = p.find_element(By.CLASS_NAME, 'itemImg').find_element(By.TAG_NAME, 'img').get_attribute('src')
            except NoSuchElementException:
                url = "empty"
            if tid == 1:
                em_11['prod_list'].append({
                    "name": t[0],
                    "price": t[1],
                    "image": url
                })
            elif tid == 2:
                em_21['prod_list'].append({
                    "name": t[0],
                    "price": t[1],
                    "image": url
                })

        if i != end_page:
            go_btn = driver.find_element(By.CLASS_NAME, 'next')
            go_btn.click()

    driver.quit()
    return


if __name__ == "__main__":
    cth1 = Thread(target=cu_worker, args=[1])
    cth2 = Thread(target=cu_worker, args=[2])
    sth1 = Thread(target=se_worker, args=[1])
    sth2 = Thread(target=se_worker, args=[2])
    gth1 = Thread(target=gs_worker, args=[1])
    gth2 = Thread(target=gs_worker, args=[2])
    eth1 = Thread(target=em_worker, args=[1])
    eth2 = Thread(target=em_worker, args=[2])
    cth1.start()
    cth2.start()
    sth1.start()
    sth2.start()
    gth1.start()
    gth2.start()
    eth1.start()
    eth2.start()
    cth1.join()
    cth2.join()
    sth1.join()
    sth2.join()
    gth1.join()
    gth2.join()
    eth1.join()
    eth2.join()

    final.append(cu_11)
    final.append(cu_21)
    final.append(se_11)
    final.append(se_21)
    final.append(gs_11)
    final.append(gs_21)
    final.append(em_11)
    final.append(em_21)

    f = open('prod_list_csv2.csv', 'r', encoding='cp949')
    rdr = csv.reader(f)
    csv_list = []

    for line in rdr:
        csv_list.append([line[0], line[1], line[2]])

    for i in range(len(final)):
        for j in range(len(final[i]['prod_list'])):
            final[i]['prod_list'][j]['PID'] = "0"

    for i in range(len(final)):
        for j in range(len(final[i]['prod_list'])):
            for k in range(len(csv_list)):
                if final[i]['prod_list'][j]['name'] == csv_list[k][1] and final[i]['brand'] == csv_list[k][2]:
                    final[i]['prod_list'][j]['PID'] = csv_list[k][0]
                    del csv_list[k]
                    break

    with open('./res.json', 'w', encoding='utf-8') as file:
        json.dump(final, file, indent='\t', ensure_ascii=False)
