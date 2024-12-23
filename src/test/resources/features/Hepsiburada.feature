Feature:Web Test Case

  Background:

    Given Setup Driver "chrome"
    And   Go to "https://www.hepsiburada.com" address
    And   Accept cookies on the page

  @test1
  Scenario: HepsiBuradaTestCase
    Given User wants to hover and click on the categories
      | Hover             | Click    |
      | Elektronik        | Tablet   |
      | Bilgisayar/Tablet | Apple    |
      |                   | 13,2 inç |
    And  I find the most expensive tablet within the provided elements, validate its price, and click on it
    And  Switch to new window
    And  Save Information on Product Detail Page for Most Expensive Tablet
    And  Add products to cart and go to cart
    And  Compare Product Details from Product Page with Cart
