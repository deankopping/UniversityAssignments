install.packages("readxl")
library("readxl")
tort <- read_excel("tortoise.data.xls")

plot(tort[5:11])

detach(tort2)
tort2 <- tort # copy data frame to tort2

tort2 <- tort2[tort2$'Mid width' < 600,] # only those rows where mid-width < 600
tort2 <- tort2[tort2$'Rear width' < 200,]
tort2 <- tort2[tort2$'Curved width' > 100,]
tort2 <- tort2[tort2$'Gular length' < 100,]
tort2 <- tort2[tort2$'Plastron length' < 180,]

plot(tort2[5:11])
cor(tort[5:11])

tort <- tort[tort$Sex == "M" | tort$Sex == "F", ]
tort$Sex <- factor(tort$Sex)
summary(tort)

par(mfrow = c(1,1))
attach(tort)
Mweight <- subset(Weight, Sex == "M")
Fweight <- subset(Weight, Sex == "F")

boxplot(Mweight)
boxplot(Fweight)

Mweight

t.test(Mweight~Fweight)
length(Mweight)
length(Fweight)

mod1 <- lm(Weight~`Mid width`,data = tort)
mod1
summary(mod1)

plot(Weight~ `Mid width`, data = tort)
abline(mod1)

mod2 <- lm(Weight ~ Length,data = tort)
summary(mod2)
?confint
pred.plim <- predict(mod2, newdata = data.frame(Length = 200), interval = "prediction")

pred.plim
?predict.lm

predict(mod2,newdata = data.frame(Length = 18),interval = "confidence")
t.test(Weight,Length = 18)
?predict.lm
