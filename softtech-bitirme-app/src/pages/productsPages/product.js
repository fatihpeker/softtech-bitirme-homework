import React from "react";
import { Menu, Icon, Grid, GridColumn, Table, Label } from "semantic-ui-react";
import {getAllProduct} from '../../api/productRequest'
import TopMenuWithLogin from "../../component/topMenuWithLogin";
import DelProduct from "../../component/deleteProduct";


class Product extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      products: {},
      currentPage: 0,
    };
  }

  componentDidMount() {
    this.getProducts();
  }

  getProducts = () => {
    const pageSize = 5
    const pageNumber = this.state.currentPage
    // const token = sessionStorage.getItem("token");
    getAllProduct(pageNumber,pageSize)
    .then((r) => {
      if (r.data.success) {
        return r;
      }
      if (r.status === 401 || r.status === 403 || r.status === 500) {
        return Promise.reject(new Error("Bir hata oluştu"));
      }
      return Promise.reject(new Error("Bilinmeyen bir hata oluştu"));
    })
    .then(async (r) => {
      this.setState({ products: r.data.data });
    })
    .catch((e) => {
      console.log(e.message);
    });
    
  };

  changePageTo = (i) => {
    this.setState({ currentPage: i }, this.getProducts);
  };

  render() {
    const { products } = this.state;
    const pageArray = [...Array(products.totalPages).keys()];
    return (
      <div>
        <TopMenuWithLogin></TopMenuWithLogin>
      <Grid
        textAlign="center"
        style={{ height: "100vh" }}
        verticalAlign="middle"
        columns="equal"
      >
        <GridColumn>
          <Table celled>
            <Table.Header>
              <Table.Row>
                <Table.HeaderCell>Index</Table.HeaderCell>
                <Table.HeaderCell>Name</Table.HeaderCell>
                <Table.HeaderCell>Price</Table.HeaderCell>
                <Table.HeaderCell>Category</Table.HeaderCell>
                <Table.HeaderCell>Description</Table.HeaderCell>
                <Table.HeaderCell>Stock</Table.HeaderCell>
              </Table.Row>
            </Table.Header>

            <Table.Body>
              {products &&
                products.content &&
                products.content.map((value, index) => {
                  return (
                    <Table.Row>
                      <Table.Cell>
                        <Label ribbon>{products.size * products.number + (index + 1)}</Label>
                        <DelProduct callback={this.getProducts} productId={value.productId} ></DelProduct>
                      </Table.Cell>
                      <Table.Cell>{value.name}</Table.Cell>
                      <Table.Cell>{value.priceWithKdv}</Table.Cell>
                      <Table.Cell>{value.category.categoryType}</Table.Cell>
                      <Table.Cell>{value.description}</Table.Cell>
                      <Table.Cell>{value.stock}</Table.Cell>
                    </Table.Row>
                  );
                })}
            </Table.Body>

            <Table.Footer>
              <Table.Row>
                <Table.HeaderCell colSpan="6">
                  <Menu floated="right" pagination>
                    <Menu.Item
                      onClick={() => {
                        this.changePageTo(this.state.currentPage - 1);
                      }}
                      as="a"
                      disabled={products.first}
                      icon
                    >
                      <Icon name="chevron left" />
                    </Menu.Item>
                    {pageArray.map((value, index) => {
                      return (
                        <Menu.Item
                          onClick={() => {
                            this.changePageTo(index);
                          }}
                          active={products.number === value}
                          as="a"
                        >
                          {value+1}
                        </Menu.Item>
                      );
                    })}
                    <Menu.Item
                      onClick={() => {
                        this.changePageTo(this.state.currentPage + 1);
                      }}
                      as="a"
                      disabled={products.last}
                      icon
                    >
                      <Icon name="chevron right" />
                    </Menu.Item>
                  </Menu>
                </Table.HeaderCell>
              </Table.Row>
            </Table.Footer>
          </Table>
        </GridColumn>
      </Grid>
      </div>
    );
  }
}

export default Product;
