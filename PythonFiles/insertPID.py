import csv
import json

if __name__ == '__main__':

    f = open('prod_list_csv.csv', 'r', encoding='cp949')
    rdr = csv.reader(f)
    csv_list = []

    with open('res9.json', 'r', encoding='utf-8') as f:
        json_object = json.load(f)

    for line in rdr:
        csv_list.append([line[0], line[1], line[2]])

    for i in range(len(json_object)):
        for j in range(len(json_object[i]['prod_list'])):
            json_object[i]['prod_list'][j]['PID'] = 0

    for i in range(len(json_object)):
        for j in range(len(json_object[i]['prod_list'])):
            for k in range(len(csv_list)):
                if json_object[i]['prod_list'][j]['name'] == csv_list[k][1] and json_object[i]['brand'] == csv_list[k][2]:
                    json_object[i]['prod_list'][j]['PID'] = csv_list[k][0]
                    del csv_list[k]
                    break

    with open('./res_p.json', 'w', encoding='utf-8') as file:
        json.dump(json_object, file, indent='\t', ensure_ascii=False)
