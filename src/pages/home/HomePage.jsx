import Header from "../navfoot/Header";
import BackgroundSection from "./BackgroundSection";
import CategoryFilter from "../../components/product/CategoryFilter";
import BestSellingProducts from "../../components/product/BestSellingProducts";
import NewsBlogSalesSection from "./NewsBlogSalesSection";
import Footer from "../navfoot/Footer";

const HomePage = () => {
  return (
    <>
      <Header />
      <BackgroundSection />
      <CategoryFilter />
      <BestSellingProducts />
      <NewsBlogSalesSection />
      <Footer />
    </>
  );
};

export default HomePage;
