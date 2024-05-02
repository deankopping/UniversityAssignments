#Katinka Wilkinson, Nathan Bau, Dean Kopping
#Regression project
#March 2022

library(readr)
birds <- read_csv("C:/Users/Admin/Desktop/STA2007F/Projects/Regression Project/birds.csv")
View(birds)

#QUESTION 4
#H1
m1 <- lm(density ~ protea + vegtype, data = birds)
summary(m1)

#H2a and H2b
m2a <- lm(density ~ time + vegtype + alien, data = birds)
m2b <- lm(density ~ time*alien + vegtype, data = birds)
summary(m2a)
summary(m2b)

#H3
m3 <- lm(density ~ distance + size, data = birds)
summary(m3)

#H4a and H4b
m4a <- lm(density ~ alien + altitude, data = birds)
m4b <- lm(density ~ alien + altitude + I(altitude^2), data = birds)
summary(m4a)
summary(m4b)

#QUESTION 5
modelNames = c("m1", "m2a", "m2b", "m3", "m4a", "m4b")
aics <- AIC(m1, m2a, m2b, m3, m4a, m4b)
delta.aics <- aics$AIC - min(aics$AIC)
wi <- exp(-0.5*delta.aics)/sum(exp(-0.5*delta.aics))

rounded.aics <- round(aics, digits = 3)
rounded.delta.aics <- round(delta.aics, digits = 3)
rounded.wi <- round(wi, digits = 3)

df <- data.frame(model = modelNames, df = aics$df, AIC = rounded.aics$AIC, delta_AIC = rounded.delta.aics, w = rounded.wi)
df

#BEST ONE IS M2B!
#QUESTION 6
plot(m2b)



