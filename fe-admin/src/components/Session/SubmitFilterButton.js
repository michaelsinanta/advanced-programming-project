import {Button} from "@nextui-org/react";

export const SubmitFilterButton = ( filter ) => {

    const handleSubmitFilter = () => {
        filter.setPage(1);
        if (filter.pcId !== undefined) {
            filter.setDate(null);
            filter.setPcId(filter.pcId);
        } else if (filter.date !== undefined) {
            filter.setPcId(null);
            filter.setDate(filter.date);
        } else {
            filter.setPcId(null);
            filter.setDate(null);
        }
    }


    return (
        <Button color="secondary" onPress={handleSubmitFilter} flat auto>
            Go
        </Button>
    );
}