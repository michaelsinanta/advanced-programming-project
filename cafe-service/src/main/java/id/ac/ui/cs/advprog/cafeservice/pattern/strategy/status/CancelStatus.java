package id.ac.ui.cs.advprog.cafeservice.pattern.strategy.status;

import id.ac.ui.cs.advprog.cafeservice.exceptions.OrderDetailStatusInvalid;
import id.ac.ui.cs.advprog.cafeservice.model.menu.MenuItem;
import id.ac.ui.cs.advprog.cafeservice.model.order.OrderDetails;
import id.ac.ui.cs.advprog.cafeservice.model.order.Status;
import id.ac.ui.cs.advprog.cafeservice.repository.MenuItemRepository;
import id.ac.ui.cs.advprog.cafeservice.service.OrderServiceImpl;

public class CancelStatus implements StatusStrategy {

    OrderDetails orderDetails;
    OrderServiceImpl orderService;
    MenuItemRepository menuItemRepository;

    public CancelStatus(OrderDetails orderDetails, OrderServiceImpl orderService, MenuItemRepository menuItemRepository) {
        this.orderDetails = orderDetails;
        this.orderService = orderService;
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public void setStatus() {
        if (orderDetails.getStatus().equals(Status.CONFIRM.getValue()) && orderDetails.getTotalPrice() != 0) {
            MenuItem menuItem = orderDetails.getMenuItem();
            menuItem.setStock(menuItem.getStock() + orderDetails.getQuantity());
            menuItemRepository.save(menuItem);
            orderDetails.setStatus(Status.CANCEL.getValue());
        } else {
            throw new OrderDetailStatusInvalid(orderDetails.getId());
        }
    }
}
