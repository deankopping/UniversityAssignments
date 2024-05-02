install.packages("readxl")
library("readxl")
tort <- read_excel("tortoise.data.xls")

?pairs
attach(tort)
pairs(~Weight + Length + `Plastron length`+ `Gular length`+ `Mid width`+`Curved width`+`Rear width`)

tort.cor <- cor(tort[5:11])
tort.cor

tort <- tort[tort$Sex == "M" | tort$Sex == "F", ]
tort$Sex <- factor(tort$Sex)

Mod1 <- lm(Weight~Length +`Curved width`,data = tort)
attach(tort)
summary(Length)
summary(`Curved width`)

?predict
predict(Mod1,data.frame(Length = 192.0,`Curved width`= 216.0))
?data.frame
mod2 <- lm(tort$Weight~tort$Length)
predict(mod2,data.frame(Length = 192.0))

confint.lm(mod2,level = 0.95)
summary(Mod1)

??visreg
install.packages("visreg")
visreg(Mod1)
library(visreg)
visreg(Mod1)  
