import csv
import json

if __name__ == '__main__':

    f = open('sep2.csv', 'r', encoding='utf-8')
    rdr = csv.reader(f)

    csv_list = []

    with open('res.json', 'r', encoding='utf-8') as f:
        json_object = json.load(f)
    print(len(json_object))
    print(type(json_object[0]['prod_list'][0]['name']))
    for line in rdr:
        if '' in line:
            line = ' '.join(line).split()
        for i in range(len(line)//3):
            csv_list.append([line[i*3+1], line[0]])
    print(type(csv_list[0][0]))


    for i in range(len(json_object)):
        for j in range(len(json_object[i]['prod_list'])):
            for k in range(len(csv_list)):
                if json_object[i]['prod_list'][j]['name'] == csv_list[k][0]:
                    json_object[i]['prod_list'][j]['PID'] = csv_list[k][1]
                    del csv_list[k]
                    break

    with open('./res2.json', 'w', encoding='utf-8') as file:
        json.dump(json_object, file, indent='\t', ensure_ascii=False)
