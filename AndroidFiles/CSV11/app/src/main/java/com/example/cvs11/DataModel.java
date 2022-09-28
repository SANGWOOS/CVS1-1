package com.example.cvs11;

public class DataModel {
    String brand;
    String type;
    Information[] prod_list;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Information[] getProd_list() {
        return prod_list;
    }

    public void setProd_list(Information[] prod_list) {
        this.prod_list = prod_list;
    }

    static public class Information {
        String name;
        String price;
        String image;
        String pid;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
        
        public String getPid() { 
            return pid; 
        }

        public void setPid(String pid) { 
            this.pid = pid; 
        }
    }
}
