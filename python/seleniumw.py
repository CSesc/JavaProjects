import time
import unittest

from selenium import webdriver
from webdriver_manager.chrome import ChromeDriverManager

from selenium.webdriver.common.keys import Keys


options = webdriver.ChromeOptions()
options.add_argument('--no-sandbox')
options.add_argument('disable-web-security')
options.add_argument('ignore-certificate-errors')

kwargs = {}; kwargs.update({'options': options})

driver = browser = webdriver = webdriver.Chrome(ChromeDriverManager().install())
driver.implicitly_wait(10) # seconds


webdriver.get('https://covidrelief.glideapp.io/dl/ewAiAHQAIgA6ADAALAAiAHMAIgA6ACIAbQBlAG4AdQAtADIAMQBlADcANgA4ADkANwAtAGQANgBiADYALQA0ADQANgAzAC0AOABmAGMAMQAtADcAMwA1ADAAOQAxADgAMABlADgAZgA2AC0AMQBkAGYAOQBmAGUAYgAxAGQANABmAGMAYQAzAGYAZgA4AGYAOAA3ADEAOAA1ADkAMgA1ADIAZABiAGMAZQAwACIALAAiAHIAIgA6ACIAZABIAE4ASQBYAGQAYgB2AFEARgB1AGYALQB4AG0AdgBRAHUATwBjADAAZwAiACwAIgBuACIAOgAiAEQAZQBsAGgAaQAiAH0A')
body=browser.find_element_by_xpath("//body")
bed_list = []
counter = 0
browser.find_element_by_xpath("//div[@class='summary-title']").click()
for i in range(500):
    box = browser.find_element_by_xpath("//div[@class='ReactVirtualized__Grid__innerScrollContainer']")
									     
    beds = box.find_elements_by_xpath("//div[@role='row']")
    for bed in beds:
        if bed.text not in bed_list:
            bed_list.append(bed.text)
    body.send_keys(Keys.ARROW_DOWN)